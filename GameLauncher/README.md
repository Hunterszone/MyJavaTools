# GameLauncher
Game launching tool for Java-based games

# How it works:

The tool automatically creates the required directories and downloads the desired resources, based on pre-defined text files, that you've already created and pushed to your remote repository.   
These files are read line by line, as you'd like to have every single resource name on a separate line.   
You will find an example inside the project repo.   

 

# Initial structure:

The tool consists of two classes:   

**Launcher.java**, responsible for the online streaming and loading of the downloaded resources   
**UpdateLog.java**, responsible for the generation and beautifying of the created log file,   
containing all streamed resources and their modifications   

The default resource files and folders are:   

**images/images.txt** - contains the description of the images, that are used   
**sounds/sounds.txt** - contains the description of the sounds, that are used   
**version.txt** - contains the version of the compiled game;   
if the local version matches the version in the remote repository, then "up-to-date" is written inside the local version.txt   

NB.: Of course, you could extend this structure further, so that it fits to your project.
