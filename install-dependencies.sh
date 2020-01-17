#!/bin/bash

declare -a VERSIONS=(
  1.7.2 1.7.8 1.7.10
  1.8 1.8.3 1.8.8
  1.9.2 1.9.4
  1.10.2
  1.11.2
  1.12
  1.13 1.13.2
  1.14 1.14.4
)

if [[ ! -d "lib" ]]; then
  mkdir "lib"
fi

for i in "${VERSIONS[@]}"; do
  FILE="craftbukkit-$i.jar"

  if [[ ! -f "lib/$FILE" ]]; then
    echo "Downloading $FILE..."
    curl "http://static.azoraqua.com/craftbukkit/craftbukkit-$i.jar" -s -o "$FILE"
    mv "$FILE" "lib/$FILE"
  fi

  mvn install:install-file -Dfile="lib/$FILE" -Dpackaging=jar -DgroupId=org.bukkit -DartifactId=craftbukkit -Dversion="$i"
  echo "Installed $FILE to your local Maven repository."
  rm "lib/$FILE"
done
