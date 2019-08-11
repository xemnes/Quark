from jsongen import *
import re

# Use category=... and flag=... to set the flags for this one

copy([
	('block_model_slab.json', 'assets/{modid}/models/block/{name}_slab.json'),
	('block_model_slab_top.json', 'assets/{modid}/models/block/{name}_slab_top.json'),
	('block_item_slab.json', 'assets/{modid}/models/item/{name}_slab.json'),
	('blockstate_slab.json', 'assets/{modid}/blockstates/{name}_slab.json'),

	('block_model_stairs.json', 'assets/{modid}/models/block/{name}_stairs.json'),
	('block_model_stairs_outer.json', 'assets/{modid}/models/block/{name}_stairs_outer.json'),
	('block_model_stairs_inner.json', 'assets/{modid}/models/block/{name}_stairs_inner.json'),
	('block_item_stairs.json', 'assets/{modid}/models/item/{name}_stairs.json'),
	('blockstate_stairs.json', 'assets/{modid}/blockstates/{name}_stairs.json'),

	('recipe_slab.json', 'data/{modid}/recipes/{category}/slabs/{name}_slab.json'),
	('recipe_stairs.json', 'data/{modid}/recipes/{category}/stairs/{name}_stairs.json')
])

localize((
	lambda name, modid: 'block.{modid}.{name}_slab'.format(name = name, modid = modid),
	lambda name, modid: re.sub(r's$', '', localize_name(name, modid)) + ' Slab'
))

localize((
	lambda name, modid: 'block.{modid}.{name}_stairs'.format(name = name, modid = modid),
	lambda name, modid: re.sub(r's$', '', localize_name(name, modid)) + ' Stairs'
))