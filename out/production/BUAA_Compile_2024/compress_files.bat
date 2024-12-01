@echo off
set zipFile=res.zip
set sourceFiles=Compiler.java config.json frontend midend backend io llvm_test mipsTest compress_files.bat



REM 检查是否存在旧的压缩文件，若存在则删除
if exist %zipFile% del %zipFile%

REM 压缩文件和文件夹，包含当前批处理脚本，并保留 `llvm_test` 文件夹结构
tar -a -c -f %zipFile% %sourceFiles% %scriptFile%

echo 文件已成功压缩为 %zipFile%，包括当前批处理脚本和 llvm_test 目录内容
