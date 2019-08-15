import os

def main():
	walls = []
	slabs = []
	stairs = []

	for file in os.listdir('../assets/quark/blockstates'):
		if '.json' in file:
			name = file.replace('.json', '')
			if '_wall' in name:
				walls.append(name)
			elif '_slab' in name:
				slabs.append(name)
			elif '_stairs' in name:
				stairs.append(name)

	write_file('items/walls.json', walls)
	write_file('items/slabs.json', slabs)
	write_file('items/stairs.json', stairs)

	write_file('blocks/walls.json', walls)
	write_file('blocks/slabs.json', slabs)
	write_file('blocks/stairs.json', stairs)

def write_file(filename, arr): 
	first = True
	with open('../data/minecraft/tags/' + filename, 'w') as writer:
		writer.write('{\n')
		writer.write('  "replace": false,\n')
		writer.write('  "values": [\n')
		for name in arr:
			if not first:
				writer.write(',\n')
			else:
				first = False
			writer.write('    "quark:' + name + '"')
		writer.write('\n  ]\n')
		writer.write('}\n')

main()