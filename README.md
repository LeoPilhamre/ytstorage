# ytstorage

# Created by https://www.toptal.com/developers/gitignore/api/scala,sbt,visualstudiocode,bloop,metals
# Edit at https://www.toptal.com/developers/gitignore?templates=scala,sbt,visualstudiocode,bloop,metals

### Bloop ###
.bloop/

### Metals ###
.metals/
project/**/metals.sbt

### SBT ###
# Simple Build Tool
# http://www.scala-sbt.org/release/docs/Getting-Started/Directories.html#configuring-version-control

dist/*
target/
lib_managed/
src_managed/
project/boot/
project/plugins/project/
.history
.cache
.lib/

### SBT Patch ###
.bsp/

### Scala ###
*.class
*.log

# virtual machine crash logs, see http://www.java.com/en/download/help/error_hotspot.xml
hs_err_pid*

### VisualStudioCode ###
.vscode/*
!.vscode/settings.json
!.vscode/tasks.json
!.vscode/launch.json
!.vscode/extensions.json
!.vscode/*.code-snippets

# Local History for Visual Studio Code
.history/

# Built Visual Studio Code Extensions
*.vsix

### VisualStudioCode Patch ###
# Ignore all local history of files
.ionide

# End of https://www.toptal.com/developers/gitignore/api/scala,sbt,visualstudiocode,bloop,metals
