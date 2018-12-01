BUKKIT=../bukkit-1.13.1B1.jar
PROTOCOLLIB=../ProtocolLib.jar
JAVA=1.8
PLUGIN=Misc
TARGET_DIR=net

JFLAGS = -Xlint:all -classpath $(BUKKIT):$(PROTOCOLLIB) -d ./ -source $(JAVA) -target $(JAVA)
JC = javac
SOURCEFILES = $(wildcard src/*.java)

default: jar_file

class_files:
	$(JC) $(JFLAGS) $(SOURCEFILES)

jar_file: class_files
	jar -cfe ./$(PLUGIN).jar ./*


clean:
	rm -f *.jar
	rm -rf $(TARGET_DIR)
