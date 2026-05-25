#!/bin/bash
echo "Ingresa tu API key de Anthropic (sk-ant-...) o presiona Enter para omitir:"
read -r ANTHROPIC_API_KEY
if [ -n "$ANTHROPIC_API_KEY" ]; then
  java -DANTHROPIC_API_KEY="$ANTHROPIC_API_KEY" -jar target/proptech.jar
else
  java -jar target/proptech.jar
fi
