/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package volume;

import java.io.File;
import java.io.IOException;

/**
 *
 * @author michel
 *  Modified by Anna Vilanova
 */
public class Volume {
    
	//////////////////////////////////////////////////////////////////////
	///////////////// TO BE IMPLEMENTED //////////////////////////////////
	//////////////////////////////////////////////////////////////////////
	
    //This function linearly interpolates the value g0 and g1 given the factor (t) 
    //the result is returned. You can use it to tri-linearly interpolate the values 
    private double interpolate(double g0, double g1, double factor) {
        double result = g0*(1-factor) + g1*factor;
        return result; 
    }
    
    private double bilinearInterpolate(double g00, double g01, double g10, double g11, double factor0, double factor1){
        double fxy1 = interpolate(g00, g10, factor0);
        double fxy2 = interpolate(g01, g11, factor0);
        double result = interpolate(fxy1, fxy2, factor1);
        return result;
    }
	
    //You have to implement the trilinear interpolation of the volume
    //First implement the interpolated function above
    // At the moment the function does takes just the lowest voxel value
    // to trilinear interpolation
    public short getVoxelLinearInterpolate(double[] coord) {
        /* notice that in this framework we assume that the distance between neighbouring voxels is 1 in all directions*/
        int x = (int) Math.floor(coord[0]);
        int y = (int) Math.floor(coord[1]);
        int z = (int) Math.floor(coord[2]);
        double t0 = coord[0] - x;
        double t1 = coord[1] - y;
        double t2 = coord[2] - z;
        
        short g000 = getVoxel(x, y, z);
        short g100 = getVoxel(x+1, y, z);
        short g010 = getVoxel(x, y+1, z);
        short g110 = getVoxel(x+1, y+1, z);
        short g001 = getVoxel(x, y, z+1);
        short g101 = getVoxel(x+1, y, z+1);
        short g011 = getVoxel(x, y+1, z+1);
        short g111 = getVoxel(x+1, y+1, z+1);
        
        double bottomInterpolation = bilinearInterpolate(g000, g100, g010, g110, t0, t1);
        double topInterpolation = bilinearInterpolate(g001, g101, g011, g111, t0, t1);
        double result = interpolate(bottomInterpolation, topInterpolation, t2);
        
        return (short)result;            
    }
		
    //////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////

    //Do NOT modify this function
    // This function is an example and does a nearest neighbour interpolation
    public short getVoxelNN(double[] coord) {
        if (coord[0] < 0 || coord[0] > (dimX-1) || coord[1] < 0 || coord[1] > (dimY-1)
                || coord[2] < 0 || coord[2] > (dimZ-1)) {
            return 0;
        }
        /* notice that in this framework we assume that the distance between neighbouring voxels is 1 in all directions*/
        int x = (int) Math.round(coord[0]); 
        int y = (int) Math.round(coord[1]);
        int z = (int) Math.round(coord[2]);
    
        return getVoxel(x, y, z);
    }
	
	//Do NOT modify this function
    public Volume(int xd, int yd, int zd) {
        data = new short[xd*yd*zd];
        dimX = xd;
        dimY = yd;
        dimZ = zd;
    }
	//Do NOT modify this function
    public Volume(File file) {
        
        try {
            VolumeIO reader = new VolumeIO(file);
            dimX = reader.getXDim();
            dimY = reader.getYDim();
            dimZ = reader.getZDim();
            data = reader.getData().clone();
            computeHistogram();
        } catch (IOException ex) {
            System.out.println("IO exception");
        }
        
    }
    
	//Do NOT modify this function
    public short getVoxel(int x, int y, int z) {
    	int i = x + dimX*(y + dimY * z);
        return data[i];
    }
    
	//Do NOT modify this function
    public void setVoxel(int x, int y, int z, short value) {
    	int i = x + dimX*(y + dimY * z);
        data[i] = value;
    }
    
	//Do NOT modify this function
    public void setVoxel(int i, short value) {
        data[i] = value;
    }
    
	//Do NOT modify this function
    public short getVoxel(int i) {
        return data[i];
    }
    
	//Do NOT modify this function
    public int getDimX() {
        return dimX;
    }
    
	//Do NOT modify this function
    public int getDimY() {
        return dimY;
    }
    
	//Do NOT modify this function
    public int getDimZ() {
        return dimZ;
    }

	//Do NOT modify this function
    public short getMinimum() {
        short minimum = data[0];
        for (int i=0; i<data.length; i++) {
            minimum = data[i] < minimum ? data[i] : minimum;
        }
        return minimum;
    }
    
	//Do NOT modify this function
    public short getMaximum() {
        short maximum = data[0];
        for (int i=0; i<data.length; i++) {
            maximum = data[i] > maximum ? data[i] : maximum;
        }
        return maximum;
    }
 
	//Do NOT modify this function
    public int[] getHistogram() {
        return histogram;
    }
    
	//Do NOT modify this function
    private void computeHistogram() {
        histogram = new int[getMaximum() + 1];
        for (int i=0; i<data.length; i++) {
            histogram[data[i]]++;
        }
    }
    
	//Do NOT modify these attributes
    private int dimX, dimY, dimZ;
    private short[] data;
    private int[] histogram;
}
