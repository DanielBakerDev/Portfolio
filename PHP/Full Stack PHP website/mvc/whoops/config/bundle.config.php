<?php

return [
    'module_name' => 'whoops',
    // 'meta_tags'
    'resources' => [
        'core_pages' => [
            'css' => [
                '../../../assets/css/bootstrap.min.css',
            ],
            'js' => [
                '../../../assets/js/jquery.min.js',
                '../../../assets/js/bootstrap.min.js',
            ]
        ],
        'sub_pages' => [
            0 => [
                'css' => [
                   
                ],
                'js' => [
                   
                ]
            ],
            'add' => [
                'css' => [

                ],
                'js' => [
                    'assets/js/add.js'
                ]
            ],
            'edit' => [
                'css' => [

                ],
                'js' => [
                    'assets/js/edit.js'
                ]
            ],
            'add-sub' => [
                'css' => [

                ],
                'js' => [
                    'assets/js/add-sub.js'
                ]
            ],
            'edit-sub' => [
                'css' => [

                ],
                'js' => [
                    'assets/js/edit-sub.js'
                ]
            ]
        ]
    ]
];

?>