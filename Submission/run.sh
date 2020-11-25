FILESIZE=$(stat -c%s "$1")
echo "$FILESIZE bytes."
./main $1
