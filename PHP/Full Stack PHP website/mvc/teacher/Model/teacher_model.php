<?php


class Teacher_Model extends Model
{
    public function __construct()
    {

        global $eqDb;
        $this->eqDb = $eqDb;
    }

	public function testCode($codeSet1, $codeSet2){
		$weightValue = 0;

		//Thinking of taking scores between 0 and 100 of closeness in different categories and doing sum 
		//of dif values between them

		//Test Exact
		if($codeSet1 == $codeSet2 && $codeSet1 != " " && $codeSet1 != null){
			$weightValue = 100;
			return [
				'r'        => 'success',
				'message'  => '<strong>Success!',
				'score'    => $weightValue
			];
		}

		//Test Size
		$size_percent = $this->testSize($codeSet1, $codeSet2);
		
		//Test variables
		$variables_percent = $this->testVariables($codeSet1, $codeSet2);

		//Test fors, foreach, trys, switches
		$elements_percent = $this->testForElements($codeSet1, $codeSet2);
		
		//Test Line by line
		$line_percent = $this->testLineByLine($codeSet1, $codeSet2);

		$weightValue = ($size_percent + $variables_percent + $elements_percent  + $line_percent) /4;

		return [
			'r'        => 'success',
			'message'  => '<strong>Success!',
			'score'    => $weightValue
		];
	}

	public function testVariables($codeSet1, $codeSet2){
		//Test variables
		//num variables and names
		$length = strlen($codeSet1);
		$codeSetArray1 = array();
		$isVar = "false";
		$wordString = "";
		for ($i=0; $i<$length; $i++) {
			if($codeSet1[$i] == "$" && $isVar == "false"){
				$isVar = "true";
			}
			if(($codeSet1[$i] == " " || $codeSet1[$i] == "=")  && $isVar == "true"){
				array_push($codeSetArray1, $wordString);
				$wordString = "";
				$isVar = "false";
			}
			if($isVar == "true"){
				$wordString .= strval($codeSet1[$i]);
			}
		}

		$length2 = strlen($codeSet2);
		$codeSetArray2 = array();
		$isVar = "false";
		$wordString = "";
		for ($i=0; $i<$length2; $i++) {
			if($codeSet2[$i] == "$" && $isVar == "false"){
				$isVar = "true";
			}
			if(($codeSet2[$i] == " " || $codeSet2[$i] == "=")  && $isVar == "true"){
				array_push($codeSetArray2, $wordString);
				$wordString = "";
				$isVar = "false";
			}
			if($isVar == "true"){
				$wordString .= strval($codeSet2[$i]);
			}
		}

		$countVariable = 0;
		$length = count($codeSetArray1);
		$length2 = count($codeSetArray2);
		$variableArray = array();

		for ($i=0; $i<$length; $i++) {
			for ($p=0; $p<$length2; $p++) {
				if($codeSetArray1[$i] == $codeSetArray2[$p]){
					array_push($variableArray, $codeSetArray2[$p]);
				}
			}
		}

		$result = array_unique($variableArray);
		if(count($result) == 0){
			return 0;
		}
		$percent = 0;

		if(count($codeSetArray1) >= count($codeSetArray2)){
			$unique = array_unique($codeSetArray1);
			$percent = (count($result)/count($unique)) * 100;
		}else{
			$unique = array_unique($codeSetArray2);
			$percent = (count($result)/count($unique)) * 100;
		}

		return $percent;
	}

	public function testSize($codeSet1, $codeSet2){
		if(strlen($codeSet1) > strlen($codeSet2)){
			$sizeOF = (strlen($codeSet2) / strlen($codeSet1)) * 100;
			return $sizeOF;
		}
		else if(strlen($codeSet1) < strlen($codeSet2)){
			$sizeOF = (strlen($codeSet1) / strlen($codeSet2)) * 100;
			return $sizeOF;
		}
		else{
			return 1;
		}
	}

	public function testForElements($codeSet1, $codeSet2){

		$keyArray = ["for", "foreach", "switch", "and", "if", "else", "count", "while", "this", "++" ];
		//Array 1
		$keyArraySet1 = ["for"    => 0, 
						"foreach" => 0, 
						"switch"  => 0, 
						"and"	  => 0, 
						"if" 	  => 0, 
						"else" 	  => 0, 
						"count"   => 0,
						"while"   => 0,
						"this"    => 0,
						"++"      => 0
				];

		$codeSetArray1 = explode( ' ', $codeSet1);

		for ($i=0; $i< count($codeSetArray1); $i++) {
			for ($p=0; $p< count($keyArray); $p++) {
				if(strpos($codeSetArray1[$i], $keyArray[$p]) !== false){
					$keyArraySet1[$keyArray[$p]] += 1;
				}
			}
		}

		//Array 2
		$keyArraySet2 = ["for"    => 0, 
						"foreach" => 0, 
						"switch"  => 0, 
						"and"	  => 0, 
						"if" 	  => 0, 
						"else" 	  => 0, 
						"count"   => 0,
						"while"   => 0,
						"this"    => 0,
						"++"      => 0
				];

		$codeSetArray2 = explode( ' ', $codeSet2);

		for ($i=0; $i< count($codeSetArray2); $i++) {
			for ($p=0; $p< count($keyArray); $p++) {
				if(strpos($codeSetArray2[$i], $keyArray[$p]) !== false){
					$keyArraySet2[$keyArray[$p]] += 1;
				}
			}
		}

		$keyArraySetTotal = ["for"    => 0, 
							"foreach" => 0, 
							"switch"  => 0, 
							"and"	  => 0, 
							"if" 	  => 0, 
							"else" 	  => 0, 
							"count"   => 0,
							"while"   => 0,
							"this"    => 0,
							"++"      => 0
					];

		$total = 0;
		for ($i=0; $i< count($keyArray); $i++) {
			
			if($keyArraySet1[$keyArray[$i]] == $keyArraySet2[$keyArray[$i]]){
				$keyArraySetTotal[$keyArray[$i]] = 10;
			}
			else {
				$dif = (int)$keyArraySet1[$keyArray[$i]] - (int)$keyArraySet2[$keyArray[$i]];
				if($dif < 0){
					$dif = $dif * -1;
				}

				if($dif >= 2 && $dif > 0)
					$keyArraySetTotal[$keyArray[$i]] = 9;
				else if($dif <= 5 && $dif >= 3)
					$keyArraySetTotal[$keyArray[$i]] = 8;
				else if($dif <= 8 && $dif >= 6)
					$keyArraySetTotal[$keyArray[$i]] = 7;
				else if($dif <= 10 && $dif >= 9)
					$keyArraySetTotal[$keyArray[$i]] = 6;
				else if($dif <= 12 && $dif >= 11)
					$keyArraySetTotal[$keyArray[$i]] = 4;
				else if($dif <= 14 && $dif >= 13)
					$keyArraySetTotal[$keyArray[$i]] = 2;
				else if($dif <= 16 && $dif >= 15)
					$keyArraySetTotal[$keyArray[$i]] = 1;

				// $keyArraySetTotal[$keyArray[$i]] = $dif;
			}
		}

		$total = 0;
		for ($i=0; $i< count($keyArray); $i++) {
			$total += $keyArraySetTotal[$keyArray[$i]];
		}

		return $total;
	}

	public function testLineByLine($codeSet1, $codeSet2){
		//how many lines are same, if longer line then hould have more weighting
		$codeSetArray1 = explode( ' ', $codeSet1);
		$codeSetArray2 = explode( ' ', $codeSet2);

		$unique = array_unique($codeSetArray1);
		$unique2 = array_unique($codeSetArray2);

		$both = array_merge($codeSetArray1, $codeSetArray2);
		$both_unique = array_unique($both);



		$arrayTotal = count($both_unique);

		$count = 0;
		$array_similar =  array();
		for ($i=0; $i< count($codeSetArray1); $i++) {
			for ($j=0; $j < count($codeSetArray2); $j++) {
				if($codeSetArray1[$i] == $codeSetArray2[$j]){
					array_push($array_similar, $codeSetArray1[$i]);
				}
			}
		}

		$arrayUnique = array_unique($array_similar);

		$count = (count($arrayUnique) / $arrayTotal) * 100;
		
		return $count;
	}
}