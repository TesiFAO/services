from osgeo import gdal
import os, glob

path = "*.hdf"
list = ""
for fname in glob.glob(path):
    try:
        gtif = gdal.Open(fname)
        print "Listing: " + gtif.GetSubDatasets()[1][0]
        list += "'" + gtif.GetSubDatasets()[1][0] + "' "
    except Exception, e:
        print fname
        pass

print list

cmd1 = "gdalwarp " + list + " PIPPO.hdf"
cmd2 = "gdalwarp -t_srs \"+proj=latlong +ellps=sphere\" PIPPO.hdf PIPPO.tif"
cmd3 = "gdal_translate -a_srs \"EPSG:4326\" PIPPO.tif PIPPO_4326.tif"
print cmd1
os.system(cmd1)
print cmd2
os.system(cmd2)
print cmd3
os.system(cmd3)