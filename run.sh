#!/bin/bash
# Start AVL Editor application

cd "$(dirname "$0")"

# Kill any existing instance
pkill -f "sbt.*run" 2>/dev/null
sleep 1

# Start the application
sbt run &
