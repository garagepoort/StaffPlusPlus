
echo "[Version bumper]"
read -p "Enter the new version " filename

if [ "$filename" = "" ]; then
	echo "Enter the version dummy" 
	return
fi
mvn versions:set -DnewVersion=$filename -DgenerateBackupPoms=false

mvn clean install