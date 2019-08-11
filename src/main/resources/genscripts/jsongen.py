import sys

modid = 'quark'
params = {}

def copy_template(name, base, target):
	base_file = 'templates/{0}'.format(base)
	target_file = '../{0}'.format(target)

	with open(base_file, 'r') as reader:
		with open(target_file, 'w') as writer:
			for line in reader:
				line = line.replace('%modid%', modid)
				line = line.replace('%name%', name)
				for param in params:
					line = line.replace('%{0}%'.format(param), params[param])

				writer.write(line)

def copy(templates):
	if 'file:' in sys.argv[1]:
		copy_file(sys.argv[1][5:], templates)
	else:
		copy_array(1, sys.argv, templates)

def copy_file(file, templates):
	lines = []
	with open(file, 'r') as reader:
		for line in reader:
			lines.append(line.strip())

	print(lines)
	copy_array(0, lines, templates)

def copy_array(start, arr, templates):
	argslen = len(arr)
	for i in range(start, argslen):
		name = arr[i]
		if '=' in name:
			parse_param(name)
		else:
			for tup in templates:
				base = tup[0]
				target = tup[1].format(modid = modid, name = name)
				copy_template(name, base, target)

def parse_param(str):
	toks = str.split('=')
	params[toks[0]] = toks[1]