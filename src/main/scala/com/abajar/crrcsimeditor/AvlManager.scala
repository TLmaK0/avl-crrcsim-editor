/*
 * Copyright (C) 2015  Hugo Freire Gil
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 */

package com.abajar.crrcsimeditor

import java.io.{File, FileOutputStream, InputStream}
import java.net.{HttpURLConnection, URL}
import java.nio.file.{Files, Path, Paths}
import java.nio.file.attribute.PosixFilePermission
import java.util.Properties
import java.util.logging.{Level, Logger}
import scala.collection.JavaConverters._

object AvlManager {

  val logger = Logger.getLogger(AvlManager.getClass.getName)

  val AVL_BASE_URL = "https://web.mit.edu/drela/Public/web/avl/"
  val AVL_DIR = System.getProperty("user.home") + "/.crrcsimeditor/avl"

  case class AvlBinary(fileName: String, url: String)

  def getAvlBinaryForOS: Option[AvlBinary] = {
    val os = System.getProperty("os.name").toLowerCase
    val arch = System.getProperty("os.arch").toLowerCase

    logger.log(Level.INFO, s"Detecting OS: $os, Architecture: $arch")

    if (os.contains("linux")) {
      Some(AvlBinary("avl", AVL_BASE_URL + "avl3.40_execs/LINUX64/avl"))
    } else if (os.contains("mac") || os.contains("darwin")) {
      // Detect ARM vs Intel Mac
      if (arch.contains("aarch64") || arch.contains("arm")) {
        Some(AvlBinary("avl", AVL_BASE_URL + "avl3.40_execs/DARWINM1/avl"))
      } else {
        Some(AvlBinary("avl", AVL_BASE_URL + "avl3.40_execs/DARWIN64/avl"))
      }
    } else if (os.contains("win")) {
      Some(AvlBinary("avl.exe", AVL_BASE_URL + "avl3.40_execs/WIN64/avl.exe"))
    } else {
      logger.log(Level.WARNING, s"Unsupported operating system: $os")
      None
    }
  }

  def getAvlPath: String = {
    val binary = getAvlBinaryForOS.getOrElse(throw new Exception("Unsupported OS"))
    s"$AVL_DIR/${binary.fileName}"
  }

  def ensureAvlAvailable(configuration: Properties): Boolean = {
    val configuredPath = Option(configuration.getProperty("avl.path"))

    configuredPath match {
      case Some(path) if new File(path).exists() && new File(path).canExecute() =>
        logger.log(Level.INFO, s"AVL already configured at: $path")
        true
      case _ =>
        logger.log(Level.INFO, "AVL not found or not configured, attempting to download...")
        downloadAndConfigureAvl(configuration)
    }
  }

  private def downloadAndConfigureAvl(configuration: Properties): Boolean = {
    getAvlBinaryForOS match {
      case Some(binary) =>
        try {
          val avlDir = new File(AVL_DIR)
          if (!avlDir.exists()) {
            avlDir.mkdirs()
            logger.log(Level.INFO, s"Created AVL directory: $AVL_DIR")
          }

          val avlPath = s"$AVL_DIR/${binary.fileName}"
          val avlFile = new File(avlPath)

          if (!avlFile.exists()) {
            logger.log(Level.INFO, s"Downloading AVL from: ${binary.url}")
            downloadFile(binary.url, avlFile)
            logger.log(Level.INFO, s"AVL downloaded to: $avlPath")
          }

          setExecutablePermissions(avlFile)

          configuration.setProperty("avl.path", avlPath)
          logger.log(Level.INFO, s"AVL configured at: $avlPath")

          true
        } catch {
          case ex: Exception =>
            logger.log(Level.SEVERE, "Failed to download AVL", ex)
            false
        }
      case None =>
        logger.log(Level.SEVERE, "Unsupported operating system for AVL download")
        false
    }
  }

  private def downloadFile(urlString: String, destinationFile: File): Unit = {
    var connection: HttpURLConnection = null
    var inputStream: InputStream = null
    var outputStream: FileOutputStream = null

    try {
      val url = new URL(urlString)
      connection = url.openConnection().asInstanceOf[HttpURLConnection]
      connection.setRequestMethod("GET")
      connection.setConnectTimeout(10000)
      connection.setReadTimeout(10000)
      connection.setInstanceFollowRedirects(true)

      val responseCode = connection.getResponseCode
      if (responseCode != HttpURLConnection.HTTP_OK) {
        throw new Exception(s"HTTP error code: $responseCode")
      }

      inputStream = connection.getInputStream
      outputStream = new FileOutputStream(destinationFile)

      val buffer = new Array[Byte](8192)
      var bytesRead = 0
      var totalBytesRead = 0L

      while ({bytesRead = inputStream.read(buffer); bytesRead != -1}) {
        outputStream.write(buffer, 0, bytesRead)
        totalBytesRead += bytesRead
      }

      logger.log(Level.INFO, s"Downloaded $totalBytesRead bytes")

    } finally {
      if (outputStream != null) outputStream.close()
      if (inputStream != null) inputStream.close()
      if (connection != null) connection.disconnect()
    }
  }

  private def setExecutablePermissions(file: File): Unit = {
    try {
      val path = file.toPath
      val permissions = Set(
        PosixFilePermission.OWNER_READ,
        PosixFilePermission.OWNER_WRITE,
        PosixFilePermission.OWNER_EXECUTE,
        PosixFilePermission.GROUP_READ,
        PosixFilePermission.GROUP_EXECUTE,
        PosixFilePermission.OTHERS_READ,
        PosixFilePermission.OTHERS_EXECUTE
      )
      Files.setPosixFilePermissions(path, permissions.asJava)
      logger.log(Level.INFO, s"Set executable permissions on: ${file.getAbsolutePath}")
    } catch {
      case ex: UnsupportedOperationException =>
        file.setExecutable(true, false)
        logger.log(Level.INFO, s"Set executable flag on: ${file.getAbsolutePath}")
      case ex: Exception =>
        logger.log(Level.WARNING, "Failed to set executable permissions", ex)
    }
  }
}
