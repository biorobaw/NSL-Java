#! /bin/csh
#-------------------------------------------------------
# amanda alexander 95/8/9
set echo

switch ($#argv)
case 0:
        echo ""
        echo "usage: headerme  "\""sccs_header_file_name"\"" "
        echo ""
        echo "Do not forget the quotes around the file name(s)"
        exit
endsw

# find files to give SCCS headers to

# find does not follow symbolic links - good
#set filenames = `find . \( -name "*.c" -o -name "*.h" -o -name "*.C" \) -print` 

set filenames = `ls *.java`

foreach fn ($filenames)
# if no SCCS, then add it
#        if (`grep \-l "SCCS" $fn` !=  $fn) then
                echo $fn
                cat $1 $fn > _tmp_
                mv _tmp_ $fn
#        endif
end
