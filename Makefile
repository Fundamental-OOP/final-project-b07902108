all: src/*.java
	javac -sourcepath src -d . src/*.java
run:
	java Main 2108
clean:
	rm *.class
