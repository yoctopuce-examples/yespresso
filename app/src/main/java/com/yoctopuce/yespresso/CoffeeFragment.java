package com.yoctopuce.yespresso;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.yoctopuce.yespresso.coffee.Coffee;
import com.yoctopuce.yespresso.coffee.CoffeeInventory;

/**
 * A fragment with a Google +1 button.
 * Activities that contain this fragment must implement the
 * {@link CoffeeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CoffeeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CoffeeFragment extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_COFFEE_NAME = "COFFEE_NAME";

    private OnFragmentInteractionListener mListener;
    private Coffee _coffee;
    private Button _distributeButton;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param name name of the coffee to display.
     * @return A new instance of fragment CoffeeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CoffeeFragment newInstance(String name) {
        CoffeeFragment fragment = new CoffeeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_COFFEE_NAME, name);
        fragment.setArguments(args);
        return fragment;
    }

    public CoffeeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            String name = getArguments().getString(ARG_COFFEE_NAME);
            _coffee = CoffeeInventory.get().getCoffee(name);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_coffee, container, false);
        ImageView img = (ImageView) view.findViewById(R.id.coffee_img);
        img.setImageResource(_coffee.getImg());
        TextView name = (TextView) view.findViewById(R.id.coffee_name);
        name.setText(_coffee.getName());
        TextView description = (TextView) view.findViewById(R.id.coffee_description);
        description.setText(_coffee.getDescription());
        TextView color = (TextView) view.findViewById(R.id.coffee_intensity);
        color.setText(String.format(getActivity().getString(R.string.intensity), _coffee.getIntensity()));
        _distributeButton = (Button) view.findViewById(R.id.distribute_button);
        _distributeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.ditribute(_coffee.getName());
            }
        });
        return view;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        void ditribute(String name);
    }

}
