from jsongen import *
import os

copy_targets = []

for file in os.listdir('../assets/quark/blockstates'):
	if '.json' in file:
		test = '../data/quark/loot_tables/blocks/' + file
		if not os.path.exists(test):
			copy_targets.append(file.replace('.json', ''))

foreach_arg_array(0, copy_targets, [('drop_table_generic.json', 'data/{modid}/loot_tables/blocks/{name}.json')], copy_callback)