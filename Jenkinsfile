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
		stage ('Pruebas') {
			steps {
			    sh 'mvn clean verify -Dspring.profiles.active=test'
			}

		}
		
		stage ('SonarQube'){
			steps {
			    sh '''
					mvn sonar:sonar \
					  -Dsonar.host.url=http://ec2-18-237-71-231.us-west-2.compute.amazonaws.com:9000 \
					  -Dsonar.login=4a222dde51e575f4e0b5dd4411fc0db5972bf792'''
		
			}

		}
		
        stage ('Compilacion y Entrega') {
            steps {
                sh 'mvn package -DskipTests=true' 
            }
        }

        stage ('Despliege') {
            steps {
                sh '''
                	echo "Desplegando en el Server"
                	jps -v | grep "${artifactId}" | awk \'{print $1}\' | xargs kill || true
					BUILD_ID=dontKillMe env SERVER.PORT=8081 nohup java -jar -Dspring.profiles.active=prod ./target/${artifactId}-${version}.jar > /dev/null 2>&1 &
                '''
            }
        }
        
    }
    
    post {
    	always {
        	junit 'target/surefire-reports/*.xml'
    	}
    }

}