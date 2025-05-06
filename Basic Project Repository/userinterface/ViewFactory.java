package userinterface;

import impresario.IModel;
import model.ScoutCollection;

//==============================================================================
public class ViewFactory {

	public static View createView(String viewName, IModel model)
	{
        switch (viewName) {
            case "TransactionChoiceView":
                return new TransactionChoiceView(model);
            case "ScoutSearchView":
                return new ScoutSearchView(model);
            case "ScoutCollectionView":
                return new ScoutCollectionView(model);
            case "UpdateScoutView":
                return new UpdateScoutView(model);
            case "ConfirmScoutRemovalView":
                return new ConfirmScoutRemovalView(model);
            case "ScanTreeBarcodeView":
                return new ScanTreeBarcodeView(model);
            case "EnterTreeInfoView":
                return new EnterTreeInfoView(model);
            case "ConfirmTreeRemovalView":
                return new ConfirmTreeRemovalView(model);
            case "UpdateTreeView":
                return new UpdateTreeView(model);
            default:
                return null;
        }
	}


	/*
	public static Vector createVectorView(String viewName, IModel model)
	{
		if(viewName.equals("SOME VIEW NAME") == true)
		{
			//return [A NEW VECTOR VIEW OF THAT NAME TYPE]
		}
		else
			return null;
	}
	*/

}