<?php

return [
    'module_name' => 'home',
    // 'meta_tags'
    'resources' => [
        'core_pages' => [
            'css' => [
                '../../../assets/css/bootstrap.min.css',
                '../../../assets/css/styles.css',
                '../../../assets/css/compile.css',
            ],
            'js' => [
                '../../../assets/js/jquery.min.js',
                '../../../assets/js/bootstrap.min.js',
                '../../../assets/js/jquery.validate.min.js',
                '../../../assets/js/jquery.form.min.js',
				'../../../assets/js/compile.js',
				'../../../assets/js/Notify.js',
            ]
        ],
        'sub_pages' => [
            0 => [
                'css' => [
                    'assets/css/index.css'
                ],
                'js' => [
                    'assets/js/index.js'
                ]
            ],
            'create' => [
                'css' => [
					'assets/css/index.css'
                ],
                'js' => [
                    'assets/js/create.js'
                ]
            ],
                
        ]
    ]
];
