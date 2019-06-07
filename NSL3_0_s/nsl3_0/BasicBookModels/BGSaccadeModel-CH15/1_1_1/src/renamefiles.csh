#!/bin/csh
#-------------------------------------------------------
# amanda a. 5/95
# This script reads in the list of files in a directory
# and attemps to rename them based on the two parameters 
# you provide it.  For every occurance of oldname in the 
# the file name, it will replace it with newname.
#
# renamefiles.csh "oldname" "newname" file_spec
#
# popular usage:
# renamefiles.csh "joe" "frog" *
#
# BUGS: should ask the user if they want the file moved.
#-------------------------------------------------------

switch ($#argv)
case 0:
case 1:
case 2:
	echo ""
        echo "Usage: renamefiles.csh "\""oldname"\"" "\""newname"\"" file_spec"
	echo ""
        exit
endsw

set old = "$argv[1]"  # quotes necessary to retain blanks
set new = "$argv[2]"
shift # remove argv1
shift # remove argv2

foreach fn ($argv[*])  # there could be many args here
	set newfilename=`echo $fn | sed s/$old/$new/g`
	if ($fn == $newfilename) then # same file name
		echo ""
	else 
		if ( -f $newfilename) then  #do not overwrite files
			echo "Do not want to overwrite file:"
			echo $newfilename
		else
			echo $newfilename
			mv $fn $newfilename
		endif
	endif
end
echo ""
echo "BYE"
echo ""
