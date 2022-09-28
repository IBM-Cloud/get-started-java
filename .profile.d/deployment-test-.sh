#!/bin/sh
echo "Installing audit-1.0"
export PATH=$PATH:$HOME/app/.java/jre/bin

$HOME/app/.liberty/bin/installUtility install audit-1.0 --acceptLicense