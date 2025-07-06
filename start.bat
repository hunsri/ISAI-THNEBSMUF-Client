echo "START!"

echo "launching server..."
start cmd /C "java -Djava.library.path=lib/lib/native -jar "lib\thnebsmuf.jar""

echo "launching client 1"
start cmd /K "cd AClient & java -cp "bin;lib\thnebsmuf.jar" AClient"
@REM start cmd /K "cd AClient & java -cp "bin;lib\thnebsmuf.jar" DemoClient"
echo "launching client 2"
start cmd /C "cd AClient & java -cp "bin;lib\thnebsmuf.jar" DemoClient"
echo "launching client 3"
start cmd /C "cd AClient & java -cp "bin;lib\thnebsmuf.jar" DemoClient"