/* Utility function to fetch from the folder properties the name of the cloud enviroment that student<nn>
 * is allowed to use. The value is "k8s-devops<nn>"
*/
def get_kubernetes_student_cloud() {
  def cloudName;
  withFolderProperties {
    cloudName = env.CLOUD_ENV
  }
  return cloudName
}

pipeline {
  // definition of the agent that will run the pipeline
  agent {
    kubernetes {
      cloud get_kubernetes_student_cloud()
      yamlFile 'agents/jenkins-kaniko-agent.yaml'
    }
  }
  stages {
    stage("Build Application") {
      steps {
        sh 'echo "Building the application"'
        updateGitlabCommitStatus name: 'build', state: 'running'
        container(name: 'maven') {
          sh 'mvn clean verify'
        }
        archiveArtifacts artifacts: 'target/user-service-0.0.1-SNAPSHOT.jar'
      }
    }
    stage("Code Analysis") {
      steps {
        withSonarQubeEnv(installationName: 'sonarQube-devops', credentialsId: 'sonarqube') {
          container('maven') {
            sh 'mvn sonar:sonar \
                      -Dsonar.projectKey=devops32-user-service \
                      -Dsonar.host.url=https://sonarqube.devops-labs.it \
                      -Dsonar.login=68f954b2bea38dc36c3aaa5e1204337f7d7aedcc'
          }
        }
      }
    }
//       stage("Quality Gate") {
//         steps {
//           timeout(time: 5, unit: 'MINUTES') {
//           waitForQualityGate abortPipeline: true, webhookSecretId: 'sonarqubewh'
//           }
//         }
//       }
    stage('Build Image') {
                     environment {
                         GIT_AUTH = credentials('jenkins-bot32')
                     }
                     steps {
                         sh 'echo "Building the docker image"'
                         container(name: 'kaniko'){
                             sh 'mkdir -p /kaniko/.docker'
                             sh 'cat config.json | sed "s/TOKEN/$(echo -n ${GIT_AUTH} | base64)/g" > /kaniko/.docker/config.json'
                             sh '/kaniko/executor --dockerfile Dockerfile --context `pwd` --destination registry.devops-labs.it/students/devops32/user-service:version-${BUILD_NUMBER}'
                         }
                     }
                 }
    stage('Tag Build') {
                     environment {
                         GIT_AUTH = credentials('jenkins-bot32')
                     }
                     steps {
                         sh 'echo "Tagging the git repository"'
                         sh 'git tag -a build-${BUILD_NUMBER} -m "Tag for jenkins build ${BUILD_NUMBER}"'
                         sh 'git config --local credential.helper "!f() { echo username=\\$GIT_AUTH_USR; echo password=\\$GIT_AUTH_PSW; }; f"'
                         sh 'git push origin build-${BUILD_NUMBER}'
                     }
                 }
    stage('Deploy to Staging') {
            steps {
                withCredentials([file(credentialsId: 'userservice-ssh-keypair', variable: 'FILE')]) {
                    sh 'echo "Deploying to staging environment"'
                    sh 'chmod 400 $FILE'
                    sh 'cat src/scripts/deployContainer.sh | sed "s/{VERSION}/${BUILD_NUMBER}/g" | sed "s/{PROFILE}/staging/g" > deployContainer.sh'
                    sh 'ssh -i $FILE -o UserKnownHostsFile=src/scripts/known_hosts ec2-user@ec2-54-164-212-238.compute-1.amazonaws.com < deployContainer.sh'
                    }
                }
            }
            stage("Tag the staging deployment") {
                environment {
                    GIT_AUTH = credentials('jenkins-bot32')
                }
            steps {
                sh 'git tag -fa staging -m "Tag for deployment of build ${BUILD_NUMBER} to staging"'
                sh 'git config --local credential.helper "!f() { echo username=\\$GIT_AUTH_USR; echo password=\\$GIT_AUTH_PSW; }; f"'
                sh 'git push -f origin staging'
                }
            }

    stage('Tag Repository') {
      environment {
        GIT_AUTH = credentials('jenkins-bot32')
      }
      steps {
        sh 'echo "Tagging the git repository"'
        sh 'git tag -a build${BUILD_NUMBER} -m "Tag for jenkins build ${BUILD_NUMBER}"'
        sh 'git config --local credential.helper "!f() { echo username=\\$GIT_AUTH_USR; echo password=\\$GIT_AUTH_PSW; }; f"'
        sh 'git push origin build${BUILD_NUMBER}'
      }
    }
  }
  post {
    failure {
      updateGitlabCommitStatus name: 'build', state: 'failed'
    }
    success {
      updateGitlabCommitStatus name: 'build', state: 'success'
    }
  }
}
