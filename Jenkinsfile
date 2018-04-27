pipeline {    
    agent any
    
    tools {
        maven 'mvn'
        jdk 'jdk8'
    }
    
    options {
    	buildDiscarder(logRotator(numToKeepStr: '5'))
    }
    
    environment {
    	groupId = readMavenPom().getGroupId()
	artifactId = readMavenPom().getArtifactId()
	version = readMavenPom().getVersion()
    }
    
    stages {
        stage ('Inicializacion') {
            steps {
                sh '''
                    echo "PATH = ${PATH}"
                    echo "M2_HOME = ${M2_HOME}"
                '''
            }
        }

        stage ('Compilacion') {
            steps {
                sh 'mvn -Dmaven.test.failure.ignore=true install -e' 
            }
        }

        stage ('Entrega') {
            steps {
		sh '''
                        echo "Dejando JAR"
                        mvn clean package
                '''
            }
        }

        stage ('Despliege') {
            steps {
              
               sh 'echo "Desplegando en el Server"'
               sh 'ps | grep wolidays | awk \'{print $1}\' | xargs kill -9 || true'
			   sh 'env SERVER.PORT=8081 nohup java -jar ./target/wolidays-0.0.1-SNAPSHOT.jar &'

             }
        }
        
    }

}