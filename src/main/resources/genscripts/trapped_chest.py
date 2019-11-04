from jsongen import *

copy([
	('unique/block_item_chest.json', 'assets/{modid}/models/item/{name}_trapped_chest.json'),
	('unique/blockstate_chest.json', 'assets/{modid}/blockstates/{name}_trapped_chest.json')
])

import update_drop_tables