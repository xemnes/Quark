from jsongen import *

copy([
	('unique/block_item_chest.json', 'assets/{modid}/models/item/{name}_trapped_chest.json'),
	('unique/blockstate_chest.json', 'assets/{modid}/blockstates/{name}_trapped_chest.json'),
	('drop_table_generic.json', 'data/{modid}/loot_tables/blocks/{name}_trapped_chest.json')
])
