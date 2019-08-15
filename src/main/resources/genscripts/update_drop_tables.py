from jsongen import *
import os

generic_targets = []
slab_targets = []

for file in os.listdir('../assets/quark/blockstates'):
	if '.json' in file:
		test = '../data/quark/loot_tables/blocks/' + file
		if not os.path.exists(test):
			block_name = file.replace('.json', '')
			if '_slab' in block_name:
				slab_targets.append(block_name)
			else: 
				generic_targets.append(block_name)

foreach_arg_array(0, generic_targets, [('drop_table_generic.json', 'data/{modid}/loot_tables/blocks/{name}.json')], copy_callback)
foreach_arg_array(0, slab_targets, [('drop_table_slab.json', 'data/{modid}/loot_tables/blocks/{name}.json')], copy_callback)