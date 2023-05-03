<?php

return [
    'module_name' => 'login',
    // 'meta_tags'
    'resources' => [
        'core_pages' => [
            'css' => [
                '../../../assets/css/bootstrap.min.css',
                '../../../assets/css/styles.css'
            ],
            'js' => [
                '../../../assets/js/jquery.min.js',
                '../../../assets/js/bootstrap.min.js',
                '../../../assets/js/jquery.validate.min.js',
				'../../../assets/js/jquery.form.min.js',
				'../../../assets/js/Notify.js',
            ]
        ],
        'sub_pages' => [
            0 => [
                'css' => [

                ],
                'js' => [
                    'assets/js/login.js'
                ]
            ]
        ]
    ]
];
