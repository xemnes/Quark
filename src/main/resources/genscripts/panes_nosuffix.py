from jsongen import *
import re

# Use category=... and flag=... to set the flags for this one

copy([
	('block_model_pane_side.json', 'assets/{modid}/models/block/{name}_side.json'),
	('block_model_pane_noside.json', 'assets/{modid}/models/block/{name}_noside.json'),
	('block_model_pane_side_alt.json', 'assets/{modid}/models/block/{name}_side_alt.json'),
	('block_model_pane_noside_alt.json', 'assets/{modid}/models/block/{name}_noside_alt.json'),
	('block_model_pane_post.json', 'assets/{modid}/models/block/{name}_post.json'),
	('block_item_flat.json', 'assets/{modid}/models/item/{name}.json'),

	('blockstate_pane.json', 'assets/{modid}/blockstates/{name}.json'),

	('recipe_pane.json', 'data/{modid}/recipes/{category}/crafting/panes/{name}.json')
])

localize((
	lambda name, modid: 'block.{modid}.{name}'.format(name = name, modid = modid),
	lambda name, modid: localize_name(name, modid)
))

import update_drop_tables
