/*
 * Copyright (C) 2015  Hugo Freire Gil
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 */

package com.abajar.avleditor.ac3d

import scala.io.Source
import scala.collection.mutable.ArrayBuffer

case class Vec3(x: Float, y: Float, z: Float)
case class Color(r: Float, g: Float, b: Float, a: Float = 1.0f)

case class AC3DMaterial(
  name: String,
  rgb: Color,
  ambient: Color,
  emissive: Color,
  specular: Color,
  shininess: Float,
  transparency: Float
)

case class AC3DSurface(
  materialIndex: Int,
  vertexIndices: Array[Int],
  uvCoords: Array[(Float, Float)]
)

case class AC3DObject(
  name: String,
  objType: String,
  vertices: Array[Vec3],
  surfaces: Array[AC3DSurface],
  children: Array[AC3DObject]
)

case class AC3DModel(
  materials: Array[AC3DMaterial],
  rootObject: AC3DObject
)

object AC3DLoader {

  def load(filePath: String): Option[AC3DModel] = {
    try {
      val lines = Source.fromFile(filePath).getLines().toArray
      var idx = 0

      // Check header
      if (lines.isEmpty || !lines(0).startsWith("AC3D")) {
        return None
      }
      idx += 1

      // Parse materials
      val materials = ArrayBuffer[AC3DMaterial]()
      while (idx < lines.length && lines(idx).startsWith("MATERIAL")) {
        parseMaterial(lines(idx)).foreach(materials += _)
        idx += 1
      }

      // Parse root object
      val (rootObj, newIdx) = parseObject(lines, idx)

      Some(AC3DModel(materials.toArray, rootObj))
    } catch {
      case e: Exception =>
        e.printStackTrace()
        None
    }
  }

  private def parseMaterial(line: String): Option[AC3DMaterial] = {
    try {
      val pattern = """MATERIAL "([^"]*)" rgb ([\d.]+) ([\d.]+) ([\d.]+)\s+amb ([\d.]+) ([\d.]+) ([\d.]+)\s+emis ([\d.]+) ([\d.]+) ([\d.]+)\s+spec ([\d.]+) ([\d.]+) ([\d.]+)\s+shi ([\d.]+)\s+trans ([\d.]+)""".r

      line match {
        case pattern(name, r, g, b, ar, ag, ab, er, eg, eb, sr, sg, sb, shi, trans) =>
          Some(AC3DMaterial(
            name,
            Color(r.toFloat, g.toFloat, b.toFloat),
            Color(ar.toFloat, ag.toFloat, ab.toFloat),
            Color(er.toFloat, eg.toFloat, eb.toFloat),
            Color(sr.toFloat, sg.toFloat, sb.toFloat),
            shi.toFloat,
            trans.toFloat
          ))
        case _ => None
      }
    } catch {
      case _: Exception => None
    }
  }

  private def parseObject(lines: Array[String], startIdx: Int): (AC3DObject, Int) = {
    var idx = startIdx
    var objType = "world"
    var name = ""
    val vertices = ArrayBuffer[Vec3]()
    val surfaces = ArrayBuffer[AC3DSurface]()
    val children = ArrayBuffer[AC3DObject]()
    var numKids = 0

    // Parse OBJECT line
    if (idx < lines.length && lines(idx).startsWith("OBJECT")) {
      objType = lines(idx).split(" ")(1)
      idx += 1
    }

    // Parse object properties
    while (idx < lines.length) {
      val line = lines(idx).trim

      if (line.startsWith("name ")) {
        name = line.substring(5).replace("\"", "")
        idx += 1
      } else if (line.startsWith("kids ")) {
        numKids = line.split(" ")(1).toInt
        idx += 1
        // Parse children
        for (_ <- 0 until numKids) {
          val (child, newIdx) = parseObject(lines, idx)
          children += child
          idx = newIdx
        }
        return (AC3DObject(name, objType, vertices.toArray, surfaces.toArray, children.toArray), idx)
      } else if (line.startsWith("numvert ")) {
        val numVerts = line.split(" ")(1).toInt
        idx += 1
        for (_ <- 0 until numVerts) {
          val parts = lines(idx).trim.split("\\s+")
          vertices += Vec3(parts(0).toFloat, parts(1).toFloat, parts(2).toFloat)
          idx += 1
        }
      } else if (line.startsWith("numsurf ")) {
        val numSurfs = line.split(" ")(1).toInt
        idx += 1
        for (_ <- 0 until numSurfs) {
          val (surf, newIdx) = parseSurface(lines, idx)
          surf.foreach(surfaces += _)
          idx = newIdx
        }
      } else if (line.startsWith("OBJECT")) {
        // Encountered a new object without kids directive
        return (AC3DObject(name, objType, vertices.toArray, surfaces.toArray, children.toArray), idx)
      } else {
        idx += 1
      }
    }

    (AC3DObject(name, objType, vertices.toArray, surfaces.toArray, children.toArray), idx)
  }

  private def parseSurface(lines: Array[String], startIdx: Int): (Option[AC3DSurface], Int) = {
    var idx = startIdx
    var materialIndex = 0
    val vertexIndices = ArrayBuffer[Int]()
    val uvCoords = ArrayBuffer[(Float, Float)]()

    // SURF line
    if (idx < lines.length && lines(idx).trim.startsWith("SURF")) {
      idx += 1
    }

    // mat line
    if (idx < lines.length && lines(idx).trim.startsWith("mat ")) {
      materialIndex = lines(idx).trim.split(" ")(1).toInt
      idx += 1
    }

    // refs line
    if (idx < lines.length && lines(idx).trim.startsWith("refs ")) {
      val numRefs = lines(idx).trim.split(" ")(1).toInt
      idx += 1
      for (_ <- 0 until numRefs) {
        val parts = lines(idx).trim.split("\\s+")
        vertexIndices += parts(0).toInt
        if (parts.length >= 3) {
          uvCoords += ((parts(1).toFloat, parts(2).toFloat))
        }
        idx += 1
      }
    }

    if (vertexIndices.nonEmpty) {
      (Some(AC3DSurface(materialIndex, vertexIndices.toArray, uvCoords.toArray)), idx)
    } else {
      (None, idx)
    }
  }

  // Get all vertices and triangles for rendering
  def getTriangles(model: AC3DModel): (Array[Float], Array[Float], Array[Int]) = {
    val allVertices = ArrayBuffer[Float]()
    val allColors = ArrayBuffer[Float]()
    val allIndices = ArrayBuffer[Int]()
    var vertexOffset = 0

    def processObject(obj: AC3DObject): Unit = {
      for (surface <- obj.surfaces) {
        val mat = if (surface.materialIndex < model.materials.length) {
          model.materials(surface.materialIndex)
        } else {
          AC3DMaterial("default", Color(0.7f, 0.7f, 0.7f), Color(0.5f, 0.5f, 0.5f),
                       Color(0, 0, 0), Color(0.3f, 0.3f, 0.3f), 32, 0)
        }

        // Triangulate polygon (fan triangulation)
        if (surface.vertexIndices.length >= 3) {
          val firstIdx = surface.vertexIndices(0)
          for (i <- 1 until surface.vertexIndices.length - 1) {
            val idx1 = surface.vertexIndices(i)
            val idx2 = surface.vertexIndices(i + 1)

            // Add vertices
            for (vIdx <- Array(firstIdx, idx1, idx2)) {
              if (vIdx < obj.vertices.length) {
                val v = obj.vertices(vIdx)
                allVertices += v.x
                allVertices += v.y
                allVertices += v.z
                allColors += mat.rgb.r
                allColors += mat.rgb.g
                allColors += mat.rgb.b
                allColors += 1.0f - mat.transparency
                allIndices += vertexOffset
                vertexOffset += 1
              }
            }
          }
        }
      }

      // Process children
      for (child <- obj.children) {
        processObject(child)
      }
    }

    processObject(model.rootObject)

    (allVertices.toArray, allColors.toArray, allIndices.toArray)
  }
}
