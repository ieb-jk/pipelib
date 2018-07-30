package com.ieb.pipeline;

void build() {
    
    stage("Checkout") {
        //checkout([$class: 'GitSCM', branches: [[name: '*/feature/ALLB-4206-Jenkins-UT']], doGenerateSubmoduleConfigurations: false, extensions: [], submoduleCfg: [], userRemoteConfigs: [[credentialsId: 'iebjk', url: 'https://github.com/IEBTrading/allbeauty.com']]])
        checkout scm
    }
	
	stage("UnitTesting") {
		echo 'PhpUnit - component testing with mock api stubs'
		sh "composer install"
		sh "./vendor/bin/phpunit --log-junit reports/phpunit.xml --coverage-clover reports/coverage.xml --coverage-html reports/coverage"
	}
	
	stage('ParallelTesting') {
		parallel (
			"StaticAnalysis" : { echo 'SonarPHP - Codesniffer, LinesOfCode, MessDetector, CopyPaste Detector, CodeBrowser, DOX' },
			"Integration" : { echo 'BrowserStack with end to end testing' },
			"LoadTesting" : { echo 'JMeter, Bench, Seige' },
			"Security" : { echo 'RIPs security scanning' }
		)
	}

    stage('CaptureResults')	{
	    publishHTML([allowMissing: false, alwaysLinkToLastBuild: false, keepAll: false, reportDir: 'reports/coverage', reportFiles: 'index.html', reportName: 'Coverage', reportTitles: ''])
    }
}