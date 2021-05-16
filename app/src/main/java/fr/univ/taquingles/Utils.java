package fr.univ.taquingles;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLES30;
import android.opengl.GLUtils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class Utils {

    // Code de la méthode récupéré sur internet
    public static int loadTexture(final Context context, final int resourceId) {
        final int[] textureHandle = new int[1];
        GLES30.glGenTextures(1, textureHandle, 0);

        if (textureHandle[0] != 0)
        {
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inScaled = false;   // No pre-scaling

            // Read in the resource
            final Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resourceId, options);

            // Bind to the texture in OpenGL
            GLES30.glBindTexture(GLES20.GL_TEXTURE_2D, textureHandle[0]);

            // Set filtering
            GLES30.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
            GLES30.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST);

            // Load the bitmap into the bound texture.
            GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);

            // Recycle the bitmap, since its data has been loaded into OpenGL.
            bitmap.recycle();
        }

        if (textureHandle[0] == 0)
        {
            throw new RuntimeException("Error loading texture.");
        }

        return textureHandle[0];
    }

    public static float[] getCouleurArray(float[] couleur, int n) {
        float[] couleurs = new float[n * 4];

        for(int i = 0; i < n * 4; i += 4){
            couleurs[i] = couleur[0];
            couleurs[i + 1] = couleur[1];
            couleurs[i + 2] = couleur[2];
            couleurs[i + 3] = couleur[3];
        }

        return couleurs;
    }

    public static FloatBuffer getCouleurBuffer(float[] couleurs, int n){

        ByteBuffer bc = ByteBuffer.allocateDirect(couleurs.length * 4);
        bc.order(ByteOrder.nativeOrder());
        FloatBuffer cb = bc.asFloatBuffer();
        cb.put(couleurs);
        cb.flip();

        return cb;
    }
}
