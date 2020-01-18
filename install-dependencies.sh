#!/bin/bash
declare -a VERSIONS=(
  1.7.2-R0.3-SNAPSHOT
  1.7.5-R0.1-SNAPSHOT
  1.7.8-R0.1-SNAPSHOT
  1.7.10-R0.1-SNAPSHOT
  1.8-R0.1-SNAPSHOT
  1.8.3-R0.1-SNAPSHOT
  1.8.8-R0.1-SNAPSHOT
  1.9.2-R0.1-SNAPSHOT
  1.9.4-R0.1-SNAPSHOT
  1.10.2-R0.1-SNAPSHOT
  1.11.2-R0.1-SNAPSHOT
  1.12-R0.1-SNAPSHOT
  1.13-R0.1-SNAPSHOT
  1.13.2-R0.1-SNAPSHOT
  1.14-R0.1-SNAPSHOT
  1.14.4-R0.1-SNAPSHOT
  1.15-R0.1-SNAPSHOT
  1.15.1-R0.1-SNAPSHOT
)

if [[ ! -d "lib" ]]; then
  mkdir "lib"
fi
cd "lib"
for i in "${VERSIONS[@]}"; do
  FILE="craftbukkit-$i.jar"

  if [[ ! -f "lib/$FILE" ]]; then
    echo "Downloading $FILE..."
    curl "http://static.azoraqua.com/craftbukkit/craftbukkit-$i.jar" -s -o "$FILE"
   
  fi

  mvn install:install-file -Dfile="$FILE" -Dpackaging=jar -DgeneratePom=true -DgroupId=org.bukkit -DartifactId=craftbukkit -Dversion="$i"
  echo "Installed $FILE to your local Maven repository."
  rm "$FILE"
done
rm "lib"