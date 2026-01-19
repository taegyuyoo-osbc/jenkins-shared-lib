def call(Map c = [:]) {
    withCredentials([string(credentialsId: ${c.snykToken}, variable: 'TOKEN')]) {
    sh """
        snyk auth $TOKEN
        snyk test \
            --severity-threshold=${c.severity ?: 'high'} \
            ${c.failOn == false ? '|| true' : ''} \
            --org=${c.snykOrg} --project-name=${c.projectName}
    """
    }
}