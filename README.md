<h1 align="center">
  <br>
  <a href="https://github.com/nghiempt/fptu-kiotfpt-server"><img src="https://assets.kiotfpt.store/logo_kiotfpt.jpg" alt="KiotFPT" width="200"></a>
  <br>
  KiotFPT
  <br>
</h1>

<h4 align="center">A comprehensive back-end solution built with <a href="https://spring.io/projects/spring-boot" target="_blank">Spring Boot</a> and <a href="https://maven.apache.org/" target="_blank">Maven</a>.</h4>

<p align="center">
  <a href="https://github.com/nghiempt/fptu-kiotfpt-server">
    <img src="https://img.shields.io/badge/version-1.0-blue.svg" alt="Version">
  </a>
  <a href="mailto:hung16132016@gmail.com">
    <img src="https://img.shields.io/badge/Contact-us-blue.svg">
  </a>
</p>

<p align="center">
  <a href="#key-features">Key Features</a> •
  <a href="#installation">Installation</a> •
  <a href="#setup">Setup</a> •
  <a href="#credits">Credits</a> •
  <a href="#license">License</a>
</p>

## Key Features

* Spring Boot Framework
  - A robust back-end framework to build enterprise-level applications.
* Maven Build System
  - Easily manage dependencies and build the project.
* JDK 8 Compatibility
  - Ensures wide compatibility with various systems.
* Database Configuration
  - Flexible setup to connect with different databases.

## Installation

Follow these steps to set up the project environment:

### Install Java Development Kit (JDK) 8:

#### Windows:
1. Download JDK 8 from the Oracle website.
2. Run the installer and follow the on-screen instructions.
3. Set `JAVA_HOME` environment variable:
   - Open System Properties > Environment Variables.
   - Add `JAVA_HOME` with the path to your JDK installation directory.
   - Add `%JAVA_HOME%\bin` to the `Path` variable.

#### macOS:
1. Install Homebrew if not already installed. Open Terminal and run:
   ```bash
   /bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh)"
   ```
2. Install JDK 8:
   ```bash
   brew install --cask adoptopenjdk8
   ```
3. Set `JAVA_HOME`:
   ```bash
   export JAVA_HOME=$(/usr/libexec/java_home -v 1.8)
   echo 'export JAVA_HOME=$(/usr/libexec/java_home -v 1.8)' >> ~/.bash_profile
   source ~/.bash_profile
   ```

#### Linux:
1. Install JDK 8 using the package manager. For Ubuntu:
   ```bash
   sudo apt update
   sudo apt install openjdk-8-jdk
   ```
2. Set `JAVA_HOME`:
   ```bash
   echo 'export JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64' >> ~/.bashrc
   echo 'export PATH=$JAVA_HOME/bin:$PATH' >> ~/.bashrc
   source ~/.bashrc
   ```

### Install Apache Maven:

#### Windows:
1. Download Maven from the official website.
2. Extract the archive to a suitable location.
3. Set `MAVEN_HOME` environment variable:
   - Open System Properties > Environment Variables.
   - Add `MAVEN_HOME` with the path to the Maven directory.
   - Add `%MAVEN_HOME%\bin` to the `Path` variable.

#### macOS:
```bash
brew install maven
```

#### Linux:
```bash
sudo apt update
sudo apt install maven
```

## Setup

### Clone the Project Repository:
```bash
git clone https://github.com/hung16132016/fptu-kiotfpt-server.git
cd fptu-kiotfpt-server
```

### Configure the Application:
Edit `src/main/resources/application.properties` to set up your database and other configurations. Note that this file is not public on GitHub; please request access via the provided [Drive link](https://drive.google.com) to obtain the file content.

### Build the Project:
```bash
mvn clean install
```

### Run the Application:
```bash
mvn spring-boot:run
```

## Credits

This project is powered by:

- [Spring Boot](https://spring.io/projects/spring-boot)
- [Apache Maven](https://maven.apache.org/)
- [Java Development Kit (JDK) 8](https://www.oracle.com/java/technologies/javase/javase-jdk8-downloads.html)

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---

> GitHub [@hung16132016](https://github.com/hung16132016)


