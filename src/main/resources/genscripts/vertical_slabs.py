from jsongen import *
import re

# Use category=... and flag=... to set the flags for this one

copy([
	('block_item_vertical_slab.json', 'assets/{modid}/models/item/{name}_vertical_slab.json'),
	('blockstate_vertical_slab.json', 'assets/{modid}/blockstates/{name}_vertical_slab.json')
])

localize((
	lambda name, modid: 'block.{modid}.{name}_vertical_slab'.format(name = name, modid = modid),
	lambda name, modid: re.sub(r's$', '', localize_name(name, modid)) + ' Vertical Slab'
))

import update_drop_tables