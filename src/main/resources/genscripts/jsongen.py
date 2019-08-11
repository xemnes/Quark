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
	argslen = len(sys.argv)
	for i in range(1, argslen):
		name = sys.argv[i]
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