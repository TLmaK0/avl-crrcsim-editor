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

import java.util.logging.LogManager;

object Main extends App {
  override def main(args: Array[String]): Unit = {
    LogManager.getLogManager().readConfiguration()
    CRRCSimEditor
  }
}

