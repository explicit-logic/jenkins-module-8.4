def testApp() {
    echo 'testing the application...'
    sh 'mvn test'
}

def deployApp() {
    echo 'deploying the application...'
}

return this
