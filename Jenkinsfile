def var

node {
  try{
    stage("Prepare"){
    sh("printenv")
      if(CHANGE_BRANCH != null)
      git(
        url: 'https://github.com/pjcalvo84/mastercloudapps-cicd.git',
        branch: CHANGE_BRANCH
      )
      else
      git(
              url: 'https://github.com/pjcalvo84/mastercloudapps-cicd.git',
              branch: BRANCH_NAME
            )
    }
    stage("Create jar"){
        sh 'mvn clean install -B -DskipTests'
    }
    stage("Test") {
        sh 'mvn test'
   }
   stage("Quality"){
       sh "mvn sonar:sonar  -Dsonar.branch=${BRANCH_NAME}"

   }
   stage("Save jar"){
       archiveArtifacts "target/*.jar"
   }
   stage('Publish') {
      sh 'mvn deploy -DskipTests'
      }
  }
   finally {
      junit "target/*-reports/TEST-*.xml"
    }
}
