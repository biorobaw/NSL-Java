#!/bin/csh -f

#-------------------------------------------------------
# modified from changelotsofwordsinfiles.csh
# amanda alexander 7/10/95
# spoelstr jacob. 5/95
# erhan oztop - added sed 8/98
#
# This script reads in the list of files in a directory
# and attemps to replace every occurance of a particular word in them 
# with another word that you provide.
# The script will only operate on text and script files.
#
# changelotsofwordsinfiles.csh file_spec
#
# a popular usage is:
# changewordinfiles.csh *
#
# Some of this script adapted from page 195 of Unix C shell field guide
# Gail and Paul Anderson
#
# This script take a file called ../CMD as input for which files to change.
# The CMD file must be present one level up from where the command is given.
#	The format is:
#	1,$s/old/new/g
#
#	example:	
#	1,$s/NslInt4d/NslInt4/g
#	1,$s/NslFloat4d/NslFloat4/g
#	1,$s/NslDouble4d/NslDouble4/g
#	1,$s/NslInt3d/NslInt3/g
#
#  
#-------------------------------------------------------

switch ($#argv)
case 0:
	echo ""
        echo "usage: changelotsofwordsinfiles.csh filestochange"
	echo ""
	echo "Bye"
        exit
endsw

set ztmp = z.z.z.tmp
#set old = "$argv[1]"  # quotes necessary to retain blanks
#set new = "$argv[2]"
#shift # remove argv1
#shift # remove argv2


foreach fn ($argv[*])  # there could be many args here
	# if not a link or bin, then convert the file
	set whatkind = `file $fn`
	#if file not reaadables, there will be no ztmp
	echo $whatkind > $ztmp
	if (`grep \-l "text" $ztmp` ==  $ztmp) then
		if (-w $fn) then #make sure you can overwrite the file
			echo "--------->" $fn
# ex editor, use real input, EOF left just
			sed -f ../CMD $fn > $fn.mod
			mv $fn.mod $fn
		endif
	endif
	rm $ztmp
end # end for each

echo ""
echo "see ya"



















