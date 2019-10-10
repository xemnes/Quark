<?php
	function main_features() {
		global $feature_data;
		write_feature_data($feature_data);
	}
	
	function write_feature_data($data) {
		$first = true;
		foreach($data as $key => $value) {
			write_category($key, $value, $first);
			$first = false;
		}
	}

	function write_category($name, $category, $displayed) {
		$class = 'feature-category';
		if($displayed)
			$class = "$class active-category";

		div($class, array('data-category' => $name ));

			div('section-header');
				write(ucfirst($name));
			pop();
			$count = sizeof($category);

			div('feature-count');
				write("($count Features)");
			pop();

			if($count == 0) {
				push('h1');
					write('No features here yet!');
				pop();
			} else {
				usort($category, "cmp_features");	
				foreach ($category as $k => $feature)
					write_feature($feature, $name);
			}
		pop();
	}

	function write_feature($feature, $category_name) {
		div('feature');
			div('feature-image');
				img("img/features/$category_name/{$feature['image']}");
			pop();

			div('feature-info');
				div('feature-header');
					div('feature-title');
						write($feature['name']);
					pop();

					div('feature-version');
						write($feature['versions']);
					pop();
				pop();

				div('feature-desc');
					foreach($feature['desc'] as $i => $paragraph)
						p($paragraph);
				pop();

				if(array_key_exists('expand', $feature)) {
					div('feature-expand');
						foreach($feature['expand'] as $i => $paragraph)
							p($paragraph);
					pop();

					div('std-button feature-expand-button');
						div('button-title');
							write('More Info');
						pop();
					pop();
				}
			pop();
		pop();
	}

	function cmp_features($f1, $f2) {
		return strcmp($f1['name'], $f2['name']);
	}

	main_features();
?>