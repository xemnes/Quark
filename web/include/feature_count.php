<?php
	function main_feature_count() {
		global $feature_data;
		
		$count = 0;
		foreach($feature_data as $k => $obj)
			$count += sizeof($obj);
		echo $count;
	}

	main_feature_count();
?>