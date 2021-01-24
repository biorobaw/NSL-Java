NSL 3.0s for Java

-=-

Requirements:

- Java 10 or greater
- Eclipse (not required but run instructions are provided only for Eclipse)

-=-

How to run the generative place cell model (for instructions with images see file `'run instructions.docx'` in the `docs` folder).

1.  After cloning, open eclipse and choose the project's base folder as eclipse's workspace
2.  Import maven projects 'nslj' and 'generative_pc_model_gonzalo'
	1. Go to 'File->Import'
	2. From the list of options, choose 'Existing Maven Projects'
	3. In the next screen, select the project's base folder as the 'root directory'
	4. Two project (pom files) should appear, select them both and click finish.
3. Create a 'run configuration' and run it:
	1. In the package explorer, right click project "generative_pc_model"
	2. From the dropdown list, choose option 'Run As -> Run Configurations...'
	3. From the list, double clic 'Java Application' to create a new configuration
	4. Choose any desired name
	5. On tab 'Main', set 'Main class' to 'com.github.biorobaw.nslj.main.NslMain' (Do not copy the apostrophes)
	6. On tab 'Arguments' set the program arguments to 'com.github.biorobaw.nslj_models.generative_pc_model_gonzalo.model.WGModel' (Do not copy the apostrophes)
	7. Clic run, a gui should soon appear
4.  Run the simulation:
	1. In the gui, choose option 'Run -> run'

-=-

Notes

* Code not required to run the model "generative_pc_model_gonzalo" waa removed from the master branch. If necessary, the original code can still be found in branch 'old_code'
