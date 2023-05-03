module.exports = function(grunt){

	grunt.initConfig({
		// less: {
		// 	development: {
		// 		files: {
		// 			'assets/public/css/style2.css': 'assets/public/scss/pages.scss',
		// 		}
		// 	},
		// },
		sass: {                              // Task
			dist: {                            // Target
				options: {                       // Target options
					style: 'compressed'
				},
				files: {
					'assets/css/style.css': [
						'assets/sass/discordTemplate.scss'
					],
				}
			}
		},
		concat: {
			corecss: {
				src: [
					'assets/css/bootstrap.min.css',
					'assets/css/style.css'
				],
				dest: 'assets/css/compile.css'
			},
			corejs: {
				src: [
					// 'assets/js/jquery.min.js',
					// 'assets/js/bootstrap.min.js',
                    // 'assets/js/discordTemplate.js',
                    // 'assets/js/jquery.form.min.js',
                    // 'assets/js/jquery.validate.min.js'
				],
				dest: 'assets/js/compile.js',
			}
		},
		cssmin: {
		  options: {
		    shorthandCompacting: true,
		    roundingPrecision: -1
		  },
		  target: {
		    files: {
                'assets/css/compile.css': ['assets/css/compile.css']
		    }
		  }
		},
		uglify: {
		  corejs: {
		    files: {
                'assets/js/compile.js': ['assets/js/compile.js',]
		    }
		  }
		},
		watch: {
		  styles: {
		    files: [ // which files to watch
		    	'assets/sass/*.scss',
		    	'assets/sass/**/*.scss'
		    ],
		    tasks: ['default'],
		    options: {
		      nospawn: true
		    }
		  }
		}
	});

	grunt.loadNpmTasks('grunt-contrib-sass');
	grunt.loadNpmTasks('grunt-contrib-concat');
	grunt.loadNpmTasks('grunt-contrib-watch');
	grunt.loadNpmTasks('grunt-contrib-cssmin');
	grunt.loadNpmTasks('grunt-contrib-uglify');

	grunt.registerTask('default', ['sass','concat','cssmin']);
};