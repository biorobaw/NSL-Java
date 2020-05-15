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
set filenames = `find . \( -name "make*" \) -print` 

foreach fn ($filenames)
# if no SCCS, then add it
                echo $fn
                cat $1 $fn > _tmp_
                mv _tmp_ $fn
end
