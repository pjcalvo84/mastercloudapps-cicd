def var
node {
  try{
    stage("Preparation") {
      git(
        url: 'https://github.com/pjcalvo84/mastercloudapps-cicd.git',
        branch: CHANGE_BRANCH
      )
      sh("printenv")

    }
    stage("Create jar"){
        sh 'mvn clean install -B -DskipTests'
    }
    stage("Test") {
        sh 'mvn test'
   }
   stage("Quality"){
        sh("printenv")
       sh "mvn sonar:sonar  -Dsonar.pullrequest.branch=${CHANGE_BRANCH} -Dsonar.pullrequest.key=${CHANGE_ID}"

   }
   stage("Save jar"){
       archiveArtifacts "target/*.jar"
   }
  }
   finally {
//       sh "docker stop \$(docker ps -aq -f 'name=db')"
//       sh "docker rm \$(docker ps -aq -f 'name=db')"
      junit "target/*-reports/TEST-*.xml"
    }
}