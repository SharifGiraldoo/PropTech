@echo off
set /p ANTHROPIC_API_KEY=Ingresa tu API key de Anthropic (sk-ant-...): 
java -DANTHROPIC_API_KEY=%ANTHROPIC_API_KEY% -jar target/proptech.jar
