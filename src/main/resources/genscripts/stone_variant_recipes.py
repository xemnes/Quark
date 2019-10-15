from jsongen import *
import re

# Use category=... and flag=... to set the flags for this one

copy([
	('unique/recipe_stone_bricks.json', 'data/{modid}/recipes/world/crafting/{name}_bricks.json'),
	('unique/recipe_stone_chiseled.json', 'data/{modid}/recipes/world/crafting/chiseled_{name}_bricks.json'),
	('unique/recipe_stone_pavement.json', 'data/{modid}/recipes/world/crafting/{name}_pavement.json'),
	('unique/recipe_stone_pillar.json', 'data/{modid}/recipes/world/crafting/{name}_pillar.json'),
])

import update_drop_tables
