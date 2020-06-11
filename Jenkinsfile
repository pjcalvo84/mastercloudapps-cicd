def var
def branch
def pr
node {
  try{
    stage("Prepare"){
       try{
            branch = CHANGE_BRANCH
       }
       catch(Exception e){
             branch = BRANCH_NAME
       }
      git(
          url: 'https://github.com/pjcalvo84/mastercloudapps-cicd.git',
          branch: branch
        )
    }
    stage("Create jar"){
        sh 'mvn clean package -B -DskipTests'
    }
    stage("Test") {
        sh 'mvn test'
   }
   stage("Quality"){
            sh "mvn sonar:sonar  -Dsonar.branch=${branch}"
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
