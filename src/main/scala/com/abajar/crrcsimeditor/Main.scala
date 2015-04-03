package com.abajar.crrcsimeditor

import java.util.logging.LogManager;

object Main extends App {
  override def main(args: Array[String]): Unit = {
    LogManager.getLogManager().readConfiguration()
    CRRCSimEditor
  }
}
