#!/bin/bash
declare -a VERSIONS=(
  1.13.2-R0.1-SNAPSHOT
)

if [[ ! -d "lib" ]]; then
  sudo mkdir "lib"
fi
cd "lib"
for i in "${VERSIONS[@]}"; do
  FILE="craftbukkit-$i.jar"

  if [[ ! -f "lib/$FILE" ]]; then
    echo "Downloading $FILE..."
    sudo curl "http://static.azoraqua.com/craftbukkit/craftbukkit-$i.jar" -s -o "$FILE"
  fi

  sudo mvn install:install-file -Dfile="$FILE" -Dpackaging=jar -DgeneratePom=true -DgroupId=org.bukkit -DartifactId=craftbukkit -Dversion="$i"
  echo "Installed $FILE to your local Maven repository."
  sudo rm "$FILE"
done
sudo rm -rf "lib"